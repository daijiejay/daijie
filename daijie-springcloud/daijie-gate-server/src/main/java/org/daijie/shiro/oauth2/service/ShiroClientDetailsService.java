package org.daijie.shiro.oauth2.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class ShiroClientDetailsService implements ClientDetailsService {

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		BaseClientDetails clientDetails = new BaseClientDetails();
		try {
//			if (clientDetails.getClientId() == null) {
//				throw new NoSuchClientException("No client with requested id: " + clientId);
//			}
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchClientException("No client with requested id: " + clientId);
		}
		return clientDetails;
	}

}
