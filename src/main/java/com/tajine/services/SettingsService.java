package com.tajine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tajine.domain.Setting;
import com.tajine.repositories.SettingsRepository;

import java.util.Optional;

@Service
public class SettingsService {

	@Autowired
	private SettingsRepository settingsRepository;

	public Setting createSetting(Setting setting) {
		Setting result = this.settingsRepository.save(setting);
		if (result.getId() != null) {
			return result;
		}
		return null;
	}

	public String getSettingValue(String name) {
		//TODO(WALID): CHECK THE OPTIONAL;
		return this.settingsRepository.findByName(name).get().getValue();
	}


	public Setting updateSetting (Setting setting) {
		Setting original = this.settingsRepository
			.findById(setting.getId()).get();
		original.setName(setting.getName());
		original.setValue(setting.getValue());
		this.settingsRepository.save(original);
		return original;
		
	}

	public void setSetting(String name, String value) {
		//TODO(WALID): CHECK THE OPTIONAL;
		Setting setting = this.settingsRepository.findByName(name).get();
		setting.setValue(value);
		this.settingsRepository.save(setting);
	}

	public String getSetting(String name) {
		Setting setting = this.settingsRepository.findByName(name).get();
		return setting.getValue();
	}
	      
	public void initializeSettingsFirstTime() {
		//TODO(walid): add other settings here, settings that i take for granted.
		Optional<Setting> setting = this.settingsRepository.findByName("innerPrinter2");
		if(setting.isPresent()) {
			return;
		}
		Setting settingObject = new Setting();
		settingObject.setName("innerPrinter2");
		settingObject.setValue("");
		 this.settingsRepository.save(settingObject);

	}

}
