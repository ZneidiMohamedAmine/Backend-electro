package org.egh.demo.repository;

import org.egh.demo.entity.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDevisRepository extends JpaRepository<Devis,Long> {
}
