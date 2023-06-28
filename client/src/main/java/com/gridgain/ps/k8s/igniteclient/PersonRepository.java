package com.gridgain.ps.k8s.igniteclient;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

@RepositoryConfig(cacheName = "PERSON")
@Repository
public interface PersonRepository extends IgniteRepository<Person,Long> {
}
