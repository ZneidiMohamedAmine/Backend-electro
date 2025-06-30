package org.egh.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequest {
    private String credential; // <- Match what Google sends
}
