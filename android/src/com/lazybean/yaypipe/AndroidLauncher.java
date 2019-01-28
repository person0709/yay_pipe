package com.lazybean.yaypipe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lazybean.yaypipe.gamehelper.AchievementType;
import com.lazybean.yaypipe.gamehelper.AdHelper;
import com.lazybean.yaypipe.gamehelper.AndroidHelper;
import com.lazybean.yaypipe.gamehelper.PlayService;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.util.IabHelper;
import com.lazybean.yaypipe.util.IabResult;
import com.lazybean.yaypipe.util.Purchase;

import java.io.IOException;

public class AndroidLauncher extends AndroidApplication implements PlayService, AdHelper, AndroidHelper {
	private RelativeLayout layout;

	private GoogleSignInClient googleSignInClient;
	private SnapshotsClient snapshotsClient;
	private Snapshot snapshot;

	private ProgressBar progressBar;

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
		MobileAds.initialize(this, getString(R.string.admob_id));
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getString(R.string.restart_ad_id));
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

		View gameView = initializeForView(new YayPipe(this), config);

		layout = new RelativeLayout(this);
		layout.addView(gameView);

		progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyle);
		progressBar.setIndeterminate(true);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		progressBar.setVisibility(View.VISIBLE);
		layout.addView(progressBar, params);

		setContentView(layout);
	}

	@Override
	public void showProgressBar() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressBar.setVisibility(View.VISIBLE);
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			}
		});
	}

	@Override
	public void hideProgressBar() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressBar.setVisibility(View.GONE);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
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
		getSnapshot(googleSignInAccount);
		Games.getGamesClient(this, googleSignInAccount).setViewForPopups(layout);
		Toast.makeText(this, "Signed in as " + googleSignInAccount.getEmail(), Toast.LENGTH_SHORT).show();
	}

	private void getSnapshot(GoogleSignInAccount googleSignInAccount){
		snapshotsClient = Games.getSnapshotsClient(this, googleSignInAccount);
		snapshotsClient.open("save", true).addOnCompleteListener(new OnCompleteListener<SnapshotsClient.DataOrConflict<Snapshot>>() {
			@Override
			public void onComplete(@NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task) {
				if (task.isSuccessful()){
					snapshot = task.getResult().getData();
				}
				hideProgressBar();
			}
		});
	}

	private void onDisconnected(){
		Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
		hideProgressBar();
	}

	/*-----------------------------GOOGLE PLAY SERVICE-----------------------------*/


	@Override
	public void startSignInIntent() {
		if (isConnectedToInternet()) {
			startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
		} else {
			Toast.makeText(this, "Offline mode", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void signInSilently() {
		if (isConnectedToInternet()) {
			googleSignInClient.silentSignIn().addOnCompleteListener(this,
					new OnCompleteListener<GoogleSignInAccount>() {
						@Override
						public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
							if (task.isSuccessful()) {
								Gdx.app.log("Tag", "SilentSignInSuccessful");

								onConnected(task.getResult());

							} else {
								Gdx.app.log("Tag", "SilentSignInUnSuccessful");
							}
						}
					});
		}
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
	}

	@Override
	public void unlockAchievement(AchievementType achievementType) {
		if (isSignedIn()) {
			GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
			switch (achievementType) {
				case WELCOME:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_welcome_to_yaypipe));
					break;

				case TOO_EASY:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_too_easy));
					break;

				case START_PLUMBING:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_start_plumbing));
					break;

				case NOT_HARD_ENOUGH:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_not_hard_enough));
					break;

				case PIPING_HOT:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_piping_hot));
					break;

				case MARIO_WILL_BE_PROUD:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_mario_will_be_proud));
					break;

				case NOVICE_PLUMBER:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_novice_plumber));
					break;

				case MEDIOCRE_PLUMBER:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_mediocre_plumber));
					break;

				case PROFESSIONAL_PLUMBER:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_professional_plumber));
					break;

				case EXPERT_PLUMBER:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_expert_plumber));
					break;

				case MASTER_PLUMBER:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_master_plumber));
					break;

				case COWARD:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_coward));
					break;

				case THINGS_HAPPEN:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_things_happen));
					break;

				case ON_FIRE:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_on_fire));
					break;

				case VERY_RESOURCEFUL:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_very_resourceful));
					break;

				case CAREFUL_PLANNER:
					Games.getAchievementsClient(this, signInAccount)
							.unlock(getString(R.string.achievement_careful_planner));
					break;
			}
		}
	}

	@Override
	public void incrementAchievement(AchievementType achievementType, int count) {
		if (isSignedIn()) {
			GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
			switch (achievementType) {
				case SORE_FINGER:
					Games.getAchievementsClient(this, signInAccount).increment(getString(R.string.achievement_sore_finger), count);
					break;

				case BE_WATER_MY_FRIEND:
					Games.getAchievementsClient(this, signInAccount).increment(getString(R.string.achievement_be_water_my_friend), count);
					break;

				case PLAY_TIME:
					Games.getAchievementsClient(this, signInAccount).increment(getString(R.string.achievement_keep_calm_and_plumb_on), count);
					Games.getAchievementsClient(this, signInAccount).increment(getString(R.string.achievement_no_pain_no_flow), count);
					Games.getAchievementsClient(this, signInAccount).increment(getString(R.string.achievement_try_hard), count);
					break;
			}
		}
	}

	@Override
	public void submitScore(int highScore) {
		if (isSignedIn()) {
			GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
			Games.getLeaderboardsClient(this, signInAccount)
					.submitScore(getString(R.string.leaderboard_high_score), highScore);
		}
		else {
			startSignInIntent();
		}
	}

	@Override
	public void showAchievement() {
		if (isSignedIn()) {
			Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
					.getAchievementsIntent()
					.addOnSuccessListener(new OnSuccessListener<Intent>() {
						@Override
						public void onSuccess(Intent intent) {
							startActivityForResult(intent, RC_ACHIEVEMENT_UI);
						}
					});

//			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
		}
		else {
			startSignInIntent();
		}
	}

	@Override
	public void showLeaderBoards() {
		if (isSignedIn()) {
			Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
					.getLeaderboardIntent(getString(R.string.leaderboard_high_score))
					.addOnSuccessListener(new OnSuccessListener<Intent>() {
						@Override
						public void onSuccess(Intent intent) {
							startActivityForResult(intent, RC_LEADERBOARD_HIGHSCORE_UI);
						}
					});
		}
		else {
			startSignInIntent();
		}
	}

	@Override
	public boolean isSignedIn() {
		return GoogleSignIn.getLastSignedInAccount(this) != null;
	}

	@Override
	public boolean isConnectedToInternet() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}

	@Override
	public void saveToSnapshot(final String json) {
		snapshot.getSnapshotContents().writeBytes(json.getBytes());

		SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder().build();

		snapshotsClient.commitAndClose(snapshot, metadataChange).addOnCompleteListener(new OnCompleteListener<SnapshotMetadata>() {
			@Override
			public void onComplete(@NonNull Task<SnapshotMetadata> task) {
				if (task.isSuccessful()){
					Toast.makeText(AndroidLauncher.this, "Save successful", Toast.LENGTH_SHORT).show();
					getSnapshot(GoogleSignIn.getLastSignedInAccount(AndroidLauncher.this));
				} else{
					Toast.makeText(AndroidLauncher.this, "Save unsuccessful", Toast.LENGTH_SHORT).show();
					hideProgressBar();
				}
			}
		});

	}

	@Override
	public byte[] loadFromSnapshot() {
		if (isSignedIn()) {
			try {
				return snapshot.getSnapshotContents().readFully();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
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
				.addTestDevice("9C551C797DB8BFB11C1585E779FCA78E")
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
