package de.nicoismaili.qontract;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

/** The main activity which hosts all Fragments and the AppBar. */
public class MainActivity extends AppCompatActivity {

  private NavController navController;
  private AppBarConfiguration appBarConfiguration;

  /**
   * Initialize the contents of the Activity's standard options menu.
   *
   * @param menu The options menu in which items are placed.
   * @return true to display the menu
   * @see #onPrepareOptionsMenu
   * @see #onOptionsItemSelected
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  /**
   * Called when the activity is starting. Performs initialization of the navigation and the appbar.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // Inflate host fragment layout with navigation component
    NavHostFragment hostFragment =
        (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    assert hostFragment != null;
    this.navController = hostFragment.getNavController();
    // Set toolbar as this activity's action bar and initialize it with NavigationUI
    Toolbar toolbar = findViewById(R.id.main_app_bar);
    setSupportActionBar(toolbar);
    this.appBarConfiguration =
        new AppBarConfiguration.Builder(this.navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, this.navController, appBarConfiguration);
  }

  /**
   * This hook is called whenever an item in the options menu is selected. Overriding this method is
   * necessary to make the AppBar dependent on the navigation.
   *
   * @param item The menu item that was selected.
   * @return boolean Return false to allow normal menu processing to proceed, true to consume it
   *     here.
   * @see #onCreateOptionsMenu
   */
  @SuppressLint("InflateParams")
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return NavigationUI.onNavDestinationSelected(item, this.navController)
        || super.onOptionsItemSelected(item);
  }

  /**
   * This method is called whenever the user chooses to navigate Up within your application's
   * activity hierarchy from the action bar. Overriding this method is also necessary to make the
   * AppBar dependent on the navigation.
   *
   * @return true if Up navigation completed successfully and this Activity was finished, false
   *     otherwise.
   */
  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(this.navController, this.appBarConfiguration)
        || super.onSupportNavigateUp();
  }
}
