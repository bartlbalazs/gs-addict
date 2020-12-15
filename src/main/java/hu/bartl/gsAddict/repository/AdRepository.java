package hu.bartl.gsAddict.repository;

import hu.bartl.gsAddict.model.Ad;
import org.springframework.data.repository.CrudRepository;

public interface AdRepository extends CrudRepository<Ad, String> {
}
