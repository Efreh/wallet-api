package com.efr.wallet_api;

import com.efr.wallet_api.service.OperationType;
import com.efr.wallet_api.dto.WalletTransactionDTORequest;
import com.efr.wallet_api.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WalletService walletService;

	@Test
	public void testGetBalance() throws Exception {
		UUID walletId = UUID.randomUUID();
		BigDecimal balance = BigDecimal.valueOf(1000);

		Mockito.when(walletService.getBalance(walletId)).thenReturn(balance);

		mockMvc.perform(get("/api/v1/wallets/" + walletId))
				.andExpect(status().isOk());
	}

	@Test
	public void testTransaction() throws Exception {
		UUID walletId = UUID.randomUUID();
		WalletTransactionDTORequest request = new WalletTransactionDTORequest();
		request.setWalletId(walletId);
		request.setOperationType(OperationType.DEPOSIT);
		request.setAmount(BigDecimal.valueOf(1000));

		Mockito.doNothing().when(walletService).transaction(Mockito.any(WalletTransactionDTORequest.class));

		mockMvc.perform(post("/api/v1/wallets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk());
	}
}
