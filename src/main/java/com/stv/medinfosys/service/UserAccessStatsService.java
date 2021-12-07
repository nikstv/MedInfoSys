package com.stv.medinfosys.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserAccessStatsService {
    void onRequest(HttpServletRequest request, HttpServletResponse response);
}
