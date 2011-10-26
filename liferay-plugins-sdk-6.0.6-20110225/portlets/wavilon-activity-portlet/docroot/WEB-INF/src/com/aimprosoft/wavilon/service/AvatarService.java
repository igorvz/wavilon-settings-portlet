package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.Attachment;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface AvatarService {
    Map<String, Attachment> getAvatars();
}
