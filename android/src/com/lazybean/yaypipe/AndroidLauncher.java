package com.lazybean.yaypipe;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.example.games.basegameutils.GameHelper;
import com.lazybean.yaypipe.gamehelper.AchievementType;
import com.lazybean.yaypipe.gamehelper.AdHelper;
import com.lazybean.yaypipe.gamehelper.PlayService;
import com.lazybean.yaypipe.util.IabHelper;
import com.lazybean.yaypipe.util.IabResult;
import com.lazybean.yaypipe.util.Purchase;

public class AndroidLauncher extends AndroidApplication implements PlayService, AdHelper {
	private GoogleSignInClient googleSignInClient;
	private GoogleSignInAccount signedInAccount;
	private SnapshotsClient snapshotsClient;

	private GameHelper gameHelper;
	private final static int requestCode = 1;
	private IabHelper mHelper;


	// TODO: 11/09/2016 key encryption
	private final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsnRdD" +
			"c4tchh/KSKUkzxuQo97xZ4jy42oAuVA/7hhMsG/gGT5m65qkDkxSx8cCMhJz/GQiZZVYjIy4qpRWtS/SIkvRCrC" +
			"Vt8L1MzYcdQlI8hCIFpZ+JBb6FOoDPrX/jO9Z+8CXTGgMgNmfYhcRjzKNuOOeZfUm1SaH0SEv7wNNpRQM0+gkvI" +
			"Y8iSKT0mX4InWSyTLx/96AWSGxvbIsDAaNiM03LuC/oVbUJpAgKz+p3ARe9Crhf34Mdqb9iX1jyCB8f6qO5NJfJ" +
			"LE04ugG508nL+uM2G4bLhodIx7lgL5akrEdtdycuRioggISZK2F5K3ptyoK6PyBcLOk2tVlyF4qQIDAQAB";

	protected InterstitialAd interstitialAd;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		config.useImmersiveMode = true;

		//Google Play Services settings

		googleSignInClient = GoogleSignIn.getClient(this,
				new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
				.requestScopes(Drive.SCOPE_APPFOLDER)
						.requestEmail()
				.build());

		signedInAccount = null;
		snapshotsClient = null;

//		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
//		gameHelper.enableDebugLog(true);
//
//		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
//			@Override
//			public void onSignInFailed() {
//				Gdx.app.log("MainActivity", "Log in failed");
//			}
//
//			@Override
//			public void onSignInSucceeded() {
//				Gdx.app.log("MainActivity", "Log in succeeded ");
//			}
//		};
//		gameHelper.setup(gameHelperListener);

		//In-app payment settings
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()){
					Gdx.app.log("IAB","setup_failed");
				} else {
					Gdx.app.log("IAB", "setup_succeeded");
				}
			}
		});

		//AdMob settings
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId("ca-app-pub-7109335218682097/7060553760");
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Gdx.app.log("Ad","Loaded");
			}

			@Override
			public void onAdClosed() {
				loadAd();
			}
		});
		loadAd();

		initialize(new YayPipe(this, this), config);
	}

	@Override
	protected void onStart() {
		super.onStart();
		startSignInIntent();
//		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
//		gameHelper.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		signInSilently();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		gameHelper.onActivityResult(requestCode, requestCode, data);

		if (requestCode == RC_SIGN_IN){
			Task<GoogleSignInAccount> task =
					GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				onConnected(account);
				Gdx.app.log("Tag", "SignInSuccessful");

			} catch (ApiException e) {
				Gdx.app.log("Tag", "SignInUnsuccessful :" + e.getMessage());

				onDisconnected();
			}
		}
	}

	private void onConnected(GoogleSignInAccount googleSignInAccount){
		signedInAccount = googleSignInAccount;
		snapshotsClient = Games.getSnapshotsClient(this, signedInAccount);
		Toast.makeText(this, "Signed in as " + signedInAccount.getEmail(), Toast.LENGTH_SHORT).show();
	}

	private void onDisconnected(){
		signedInAccount = null;
		snapshotsClient = null;
		Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
	}

	/*-----------------------------GOOGLE PLAY SERVICE-----------------------------*/


	@Override
	public void startSignInIntent() {
		startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
		

//		try
//		{
//			runOnUiThread(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					gameHelper.beginUserInitiatedSignIn();
//					Gdx.app.log("SignIn", "succeeded");
//				}
//			});
//		}
//		catch (Exception e)
//		{
//			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
//		}
	}

	@Override
	public void signInSilently() {
		googleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()){
							Gdx.app.log("Tag", "SilentSignInSuccessful");

							onConnected(task.getResult());

						} else {
							Gdx.app.log("Tag", "SilentSignInUnSuccessful");
						}
					}
				});
	}

	@Override
	public void signOut() {
		googleSignInClient.signOut().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
							Gdx.app.log("Tag", "SignOutSuccessful");
							onDisconnected();
						} else {
							Gdx.app.log("Tag", "SignOutUnsuccessful");
						}
					}
				});


//		try
//		{
//			runOnUiThread(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					gameHelper.signOut();
//					Gdx.app.log("SignOut", "succeeded");
//				}
//			});
//		}
//		catch (Exception e)
//		{
//			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
//		}
	}

	@Override
	public void unlockAchievement(AchievementType achievementType) {
		if (isSignedIn()) {
			switch (achievementType) {
				case WELCOME:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_welcome_to_yaypipe));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_welcome_to_yaypipe));
					break;

				case TOO_EASY:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_too_easy));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_too_easy));
					break;

				case START_PLUMBING:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_start_plumbing));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_start_plumbing));
					break;

				case NOT_HARD_ENOUGH:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_not_hard_enough));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_not_hard_enough));
					break;

				case PIPING_HOT:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_piping_hot));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_piping_hot));
					break;

				case MARIO_WILL_BE_PROUD:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_mario_will_be_proud));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_mario_will_be_proud));
					break;

				case NOVICE_PLUMBER:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_novice_plumber));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_novice_plumber));
					break;

				case MEDIOCRE_PLUMBER:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_mediocre_plumber));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_mediocre_plumber));
					break;

				case PROFESSIONAL_PLUMBER:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_professional_plumber));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_professional_plumber));
					break;

				case EXPERT_PLUMBER:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_expert_plumber));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_expert_plumber));
					break;

				case MASTER_PLUMBER:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_master_plumber));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_master_plumber));
					break;

				case COWARD:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_coward));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_coward));
					break;

				case THINGS_HAPPEN:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_things_happen));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_things_happen));
					break;

				case ON_FIRE:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_on_fire));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_on_fire));
					break;

				case VERY_RESOURCEFUL:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_very_resourceful));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_very_resourceful));
					break;

				case CAREFUL_PLANNER:
					Games.getAchievementsClient(this, signedInAccount)
							.unlock(getString(R.string.achievement_careful_planner));

//					Games.Achievements.unlock(gameHelper.getApiClient(),
//							getString(R.string.achievement_careful_planner));
					break;
			}
		}
	}

	@Override
	public void incrementAchievement(AchievementType achievementType, int count) {
		if (isSignedIn()) {
			switch (achievementType) {
				case SORE_FINGER:
					Games.getAchievementsClient(this, signedInAccount).increment(getString(R.string.achievement_sore_finger), count);
//					Games.Achievements.increment(gameHelper.getApiClient(), getString(R.string.achievement_sore_finger), 1);
					break;

				case BE_WATER_MY_FRIEND:
					Games.getAchievementsClient(this, signedInAccount).increment(getString(R.string.achievement_be_water_my_friend), count);
//					Games.Achievements.increment(gameHelper.getApiClient(), getString(R.string.achievement_be_water_my_friend), 1);
					break;

				case PLAY_TIME:
					Games.getAchievementsClient(this, signedInAccount).increment(getString(R.string.achievement_keep_calm_and_plumb_on), count);
					Games.getAchievementsClient(this, signedInAccount).increment(getString(R.string.achievement_no_pain_no_flow), count);
					Games.getAchievementsClient(this, signedInAccount).increment(getString(R.string.achievement_try_hard), count);

//					Games.Achievements.increment(gameHelper.getApiClient(), getString(R.string.achievement_keep_calm_and_plumb_on), 1);
//					Games.Achievements.increment(gameHelper.getApiClient(), getString(R.string.achievement_no_pain_no_flow), 1);
//					Games.Achievements.increment(gameHelper.getApiClient(), getString(R.string.achievement_try_hard), 1);
					break;
			}
		}
	}

	@Override
	public void submitScore(int highScore) {
		if (isSignedIn())
		{
			Games.Leaderboards.submitScore(gameHelper.getApiClient(),
					getString(R.string.leaderboard_high_score), highScore);
		}
		else
		{
			startSignInIntent();
		}
	}

	@Override
	public void showAchievement() {
		if (isSignedIn())
		{
			Games.getAchievementsClient(this, signedInAccount)
					.getAchievementsIntent()
					.addOnSuccessListener(new OnSuccessListener<Intent>() {
						@Override
						public void onSuccess(Intent intent) {
							startActivityForResult(intent, RC_ACHIEVEMENT_UI);
						}
					});

//			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
		}
		else
		{
			startSignInIntent();
		}
	}

	@Override
	public void showLeaderBoards() {
		if (isSignedIn())
		{
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
					getString(R.string.leaderboard_high_score)), requestCode);
		}
		else
		{
			startSignInIntent();
		}
	}

	@Override
	public boolean isSignedIn() {
		return signedInAccount != null;
//		return gameHelper.isSignedIn();
	}


	@Override
	public void saveGame(String json) {
		if (isSignedIn()){
		}
	}

	@Override
	public void loadGame() {

	}


	/*----------------------------AD HANDLE---------------------------------------*/


	@Override
	public void showAd(){
		try{
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (interstitialAd.isLoaded()){
						interstitialAd.show();
					}
					else{
						loadAd();
					}
				}
			});
		}
		catch (Exception e){
			Gdx.app.log("AdShow", "failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void loadAd() {
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice("3DED1D914666A69149B6A2E896C2661D")
				.build();
		interstitialAd.loadAd(adRequest);
	}

	@Override
	public void removeAds() {
		// Callback for when a purchase is finished
		IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
				if ( purchase == null) return;
				Gdx.app.log("IAB", "Purchase finished: " + result + ", purchase: " + purchase);

				// if we were disposed of in the meantime, quit.
				if (mHelper == null) return;

				if (result.isFailure()) {
//					complain("Error purchasing: " + result);
//					setWaitScreen(false);
					return;
				}
//            if (!verifyDeveloperPayload(purchase)) {
//                //complain("Error purchasing. Authenticity verification failed.");
//                //setWaitScreen(false);
//                return;
//            }

				Gdx.app.log("IAB", "Purchase successful.");

				if (purchase.getSku().equals(SKU_REMOVE_ADS)) {
					// bought the premium upgrade!
					Gdx.app.log("IAB", "Purchase is premium upgrade. Congratulating user.");

					// Do what you want here maybe call your game to do some update
					//
					// Maybe set a flag to indicate that ads shouldn't show anymore
					//mAdsRemoved = true;

				}
			}
		};

		mHelper.launchPurchaseFlow(this, SKU_REMOVE_ADS, RC_REQUEST,
				mPurchaseFinishedListener, "HANDLE_PAYLOADS");
	}
}
