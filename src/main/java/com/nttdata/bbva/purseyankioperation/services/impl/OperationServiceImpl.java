package com.nttdata.bbva.purseyankioperation.services.impl;

import com.nttdata.bbva.purseyankioperation.enums.OperationTypeEnum;
import com.nttdata.bbva.purseyankioperation.models.*;
import com.nttdata.bbva.purseyankioperation.repositories.IOperationRepository;
import com.nttdata.bbva.purseyankioperation.services.IOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OperationServiceImpl implements IOperationService {
	private static final Logger logger = LoggerFactory.getLogger(OperationServiceImpl.class);
	private final String topicOperationPurseYanki = "operationPurseYanki";
	private final String topicOperationP2P = "operationP2P";
	@Autowired
	private IOperationRepository repo;
	@Autowired
	RestTemplate restTemplate;

	@Override
	@KafkaListener(
			topics = topicOperationPurseYanki,
			containerFactory = "kafkaListenerContainerFactory",
			groupId = "grupo1")
	public void consumer(Event event) {
		logger.info("Inicio ConsumerServiceImpl ::: consumer");
		try {
			EventOperation eventOperation = event.getOperation();
			OpenAccount openAccount = this.findOpenAccountById(eventOperation.getOpenAccountId());
			OpenAccount openAccountTransfer = this.findOpenAccountById(eventOperation.getOpenAccountIdTransfer());

			Operation operation = Operation.builder()
					.openAccountId(eventOperation.getOpenAccountId())
					.operationType(OperationTypeEnum.T.toString())
					.amount(eventOperation.getAmount())
					.createdAt(LocalDateTime.now()).build();
			Operation operationTransfer = Operation.builder()
					.openAccountId(eventOperation.getOpenAccountIdTransfer())
					.operationType(OperationTypeEnum.D.toString())
					.amount(eventOperation.getAmount())
					.createdAt(LocalDateTime.now()).build();

			BigDecimal totalAmountAvailable = openAccount.getAmountAvailable().subtract(eventOperation.getAmount());
			openAccount.setAmountAvailable(totalAmountAvailable);

			BigDecimal totalAmountAvailableTransfer = openAccountTransfer.getAmountAvailable().add(eventOperation.getAmount());
			openAccountTransfer.setAmountAvailable(totalAmountAvailableTransfer);

			logger.info("ConsumerServiceImpl ::: before-openAccount ::: {}", openAccount.toString());
			logger.info("ConsumerServiceImpl ::: before-openAccountTransfer ::: {}", openAccountTransfer.toString());
			repo.save(operation);
			repo.save(operationTransfer);
			this.updateOpenAccount(openAccount);
			this.updateOpenAccount(openAccountTransfer);
			logger.info("ConsumerServiceImpl ::: after-openAccount ::: {}", openAccount.toString());
			logger.info("ConsumerServiceImpl ::: after-openAccountTransfer ::: {}", openAccountTransfer.toString());

		} catch (Exception ex) {
			logger.info("Error ConsumerServiceImpl ::: consumer ::: {}", ex.toString());
		} finally {
			logger.info("Fin ConsumerServiceImpl ::: consumer");
		}
	}

	@Override
	@KafkaListener(
			topics = topicOperationP2P,
			containerFactory = "kafkaListenerContainerFactoryP2P",
			groupId = "grupo1")
	public void consumerP2P(EventP2P event) {
		logger.info("Inicio ConsumerServiceImpl ::: consumerP2P");
		try {
			EventP2PRequest eventRequest = event.getEventRequest();
			OpenAccount openAccount = this.findOpenAccountById(eventRequest.getOpenAccountId());

			Operation operation = Operation.builder()
					.openAccountId(eventRequest.getOpenAccountId())
					.operationType(OperationTypeEnum.D.toString())
					.amount(eventRequest.getAmount())
					.createdAt(LocalDateTime.now()).build();

			BigDecimal buyRate = new BigDecimal("4");
			BigDecimal totalAmount = eventRequest.getAmount().multiply(buyRate);
			BigDecimal totalAmountAvailable = openAccount.getAmountAvailable().add(totalAmount);
			openAccount.setAmountAvailable(totalAmountAvailable);

			logger.info("ConsumerServiceImpl ::: before-openAccount ::: {}", openAccount.toString());
			repo.save(operation);
			this.updateOpenAccount(openAccount);
			logger.info("ConsumerServiceImpl ::: after-openAccount ::: {}", openAccount.toString());

		} catch (Exception ex) {
			logger.info("Error ConsumerServiceImpl ::: consumerP2P ::: {}", ex.toString());
		} finally {
			logger.info("Fin ConsumerServiceImpl ::: consumerP2P");
		}
	}

	@Override
	public Operation insert(Operation obj) {
		return null;
	}

	@Override
	public Operation update(Operation obj) {
		return null;
	}

	@Override
	public Operation findAll() {
		return null;
	}

	@Override
	public Operation findById(String id) {
		return null;
	}

	@Override
	public Void delete(String id) {
		return null;
	}

	private OpenAccount findOpenAccountById(String id) {
		return restTemplate.getForObject("http://localhost:7073/api/1.0.0/openaccounts/" + id, OpenAccount.class);
	}
	private void updateOpenAccount(OpenAccount obj) {
		restTemplate.put("http://localhost:7073/api/1.0.0/openaccounts/", obj);
	}
}
