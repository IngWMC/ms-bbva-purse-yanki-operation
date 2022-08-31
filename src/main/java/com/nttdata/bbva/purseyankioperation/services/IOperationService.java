package com.nttdata.bbva.purseyankioperation.services;

import com.nttdata.bbva.purseyankioperation.models.Event;
import com.nttdata.bbva.purseyankioperation.models.EventP2P;
import com.nttdata.bbva.purseyankioperation.models.Operation;

public interface IOperationService extends ICRUD<Operation, String> {
	void consumer(Event event);
	void consumerP2P(EventP2P event);
}
