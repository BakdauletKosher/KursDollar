package kurs.dollar.kz;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by BahaWood on 1/24/19.
 */
public interface CourseRepository extends CrudRepository<CourseModel, Float> {
    @Query(value = "SELECT * FROM course_model ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public CourseModel getLastCourse();

}
