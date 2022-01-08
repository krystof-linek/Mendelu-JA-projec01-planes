package cz.mendelu.xlinek.project_01.parcels;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParcelRepository extends CrudRepository<Parcel, Long> {
    List <Parcel> findAll();
}
