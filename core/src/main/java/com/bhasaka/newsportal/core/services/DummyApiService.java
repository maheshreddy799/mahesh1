package com.bhasaka.newsportal.core.services;

import java.util.List;

public interface DummyApiService {
    List<String> getFilteredDataById(int id);
    List<String> getFilteredDataByTitle(String title);
}
