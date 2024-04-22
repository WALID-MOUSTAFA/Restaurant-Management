package com.tajine.services;


import com.tajine.domain.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestSettings {
        @Autowired
        SettingsService service;

        @Test
        private  void testCreateSetting() {
                Setting setting = new Setting();
                setting.setName("outerPrinter");
                Setting result = this.service.createSetting(setting);
                Assertions.assertNotNull(result.getId());
        }

}
