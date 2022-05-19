package de.nicoismaili.qontract.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import de.nicoismaili.qontract.R;

/** A simple {@link Fragment} subclass containing the settings view. */
public class SettingsFragment extends PreferenceFragmentCompat {

  public SettingsFragment() {
    // Required empty public constructor
  }

  /**
   * Clears the contents of the Fragment host's standard options menu.
   *
   * @param menu The options menu in which you place your items.
   * @see #setHasOptionsMenu
   * @see #onPrepareOptionsMenu
   * @see #onOptionsItemSelected
   */
  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    menu.clear();
    super.onCreateOptionsMenu(menu, inflater);
  }

  /**
   * Called to do initial creation of a fragment. This is called after {@link #onAttach(Activity)}
   * and before {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
   *
   * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
   *     is the state.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  /**
   * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has returned,
   * but before any saved state has been restored in to the view. Calls upon {@link
   * PreferenceFragmentCompat#setHasOptionsMenu(boolean)} with true.
   *
   * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
   *     saved state as given here.
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);
  }

  /**
   * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment. Subclasses
   * are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either directly or via
   * helper methods such as {@link #addPreferencesFromResource(int)}.
   *
   * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
   *     is the state.
   * @param rootKey If non-null, this preference fragment should be rooted at the {@link
   *     PreferenceScreen} with this key.
   */
  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);
  }
}
