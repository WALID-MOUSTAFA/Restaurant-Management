package com.tajine.repositories;

import java.util.Optional;

import com.tajine.domain.Setting;

import org.springframework.data.repository.CrudRepository;

public interface SettingsRepository extends CrudRepository<Setting, Long> {
	public Optional<Setting> findByName(String name);
}
