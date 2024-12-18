package wdmsystem.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IArchivedOrderRepository extends JpaRepository<ArchivedOrder, Integer> {
}
