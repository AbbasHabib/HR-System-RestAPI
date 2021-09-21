package com.hrsystem.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class passwordChangeCommand {
    private String currentPassword;
    private String newPassword;
}
