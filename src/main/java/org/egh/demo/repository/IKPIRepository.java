package org.egh.demo.repository;


import org.egh.demo.entity.KPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IKPIRepository extends JpaRepository<KPI,Long> {
}
