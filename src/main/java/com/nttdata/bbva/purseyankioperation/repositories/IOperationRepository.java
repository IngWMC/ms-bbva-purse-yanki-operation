package com.nttdata.bbva.purseyankioperation.repositories;

import com.nttdata.bbva.purseyankioperation.models.Operation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IOperationRepository extends MongoRepository<Operation, String> {}
