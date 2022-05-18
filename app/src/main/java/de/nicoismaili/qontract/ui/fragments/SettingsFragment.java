package de.nicoismaili.qontract.ui.fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import de.nicoismaili.qontract.R;

/** A simple {@link Fragment} subclass. */
public class SettingsFragment extends PreferenceFragmentCompat {

  public SettingsFragment() {}

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    menu.clear();
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    super.onViewCreated(view, savedInstanceState);
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
