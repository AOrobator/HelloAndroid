package com.orobator.helloandroid.lesson18.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import com.orobator.helloandroid.lesson18.Injection;
import com.orobator.helloandroid.lesson18.R;
import com.orobator.helloandroid.lesson18.model.Repo;

public class SearchRepositoriesActivity extends AppCompatActivity {
  private static final String LAST_SEARCH_QUERY = "last_search_query";
  private static final String DEFAULT_QUERY = "Android";
  private SearchRepositoriesViewModel viewModel;
  private RecyclerView list;
  private EditText searchRepoEditText;
  private View emptyList;
  private ReposAdapter adapter = new ReposAdapter();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_repositories);

    searchRepoEditText = findViewById(R.id.search_repo);
    list = findViewById(R.id.list);
    emptyList = findViewById(R.id.emptyList);

    // get the view model
    viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
        .get(SearchRepositoriesViewModel.class);

    // add dividers between RecyclerView's row items
    DividerItemDecoration decoration =
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

    list.addItemDecoration(decoration);

    initAdapter();
    String query = DEFAULT_QUERY;
    if (savedInstanceState != null) {
      query = savedInstanceState.getString(LAST_SEARCH_QUERY);
    }

    viewModel.searchRepo(query);
    initSearch(query);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue());
  }

  private void initAdapter() {
    list.setAdapter(adapter);
    viewModel.repos.observe(this, (PagedList<Repo> repos) -> {
      int size = 0;
      if (repos != null) {
        size = repos.size();
      }
      Log.d(SearchRepositoriesActivity.class.getSimpleName(), "list: " + size + "");
      showEmptyList(size == 0);
      adapter.submitList(repos);
    });
    viewModel.networkErrors.observe(this, error ->
        Toast
            .makeText(this, "\uD83D\uDE28 Wooops " + error + "", Toast.LENGTH_LONG)
            .show()
    );
  }

  private void initSearch(String query) {
    searchRepoEditText.setText(query);

    searchRepoEditText.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_GO) {
        updateRepoListFromInput();
        return true;
      } else {
        return false;
      }
    });

    searchRepoEditText.setOnKeyListener((v, keyCode, event) -> {
      if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
        updateRepoListFromInput();
        return true;
      } else {
        return false;
      }
    });
  }

  private void updateRepoListFromInput() {
    String input = searchRepoEditText.getText().toString().trim();
    if (!TextUtils.isEmpty(input)) {
      Log.d(SearchRepositoriesActivity.class.getSimpleName(), "Got input: " + input);
      list.scrollToPosition(0);
      viewModel.searchRepo(input);
      adapter.submitList(null);
    }
  }

  private void showEmptyList(boolean show) {
    if (show) {
      emptyList.setVisibility(View.VISIBLE);
      list.setVisibility(View.GONE);
    } else {
      emptyList.setVisibility(View.GONE);
      list.setVisibility(View.VISIBLE);
    }
  }


}
