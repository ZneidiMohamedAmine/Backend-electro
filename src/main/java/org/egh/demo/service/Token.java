package org.egh.demo.service;

import java.util.Date;

public interface Token {
    Date getExpiryDate();
    String getToken(); // Added getToken() for consistency and potential logging
}
