package com.aimprosoft.wavilon.service;

import org.ektorp.AttachmentInputStream;

public interface AvatarService {
    AttachmentInputStream getAvatar(String attachmentId);
}
