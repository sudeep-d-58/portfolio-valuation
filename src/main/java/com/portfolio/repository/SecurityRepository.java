package com.portfolio.repository;

import com.portfolio.model.security.Security;

import java.util.List;

public interface SecurityRepository {

    List<Security> getAllSecurities();

    Security getSecurityByTicker(String ticker);
}
