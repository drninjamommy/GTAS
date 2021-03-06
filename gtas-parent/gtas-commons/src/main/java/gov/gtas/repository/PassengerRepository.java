/*
 * All GTAS code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.gtas.model.Disposition;
import gov.gtas.model.Passenger;

public interface PassengerRepository extends PagingAndSortingRepository<Passenger, Long>, PassengerRepositoryCustom {
    
	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	public Passenger getPassengerById(@Param("id") Long id);
	
    @Query("SELECT p FROM Passenger p WHERE UPPER(p.firstName) = UPPER(:firstName) AND UPPER(p.lastName) = UPPER(:lastName)")
    public List<Passenger> getPassengerByName(@Param("firstName") String firstName,@Param("lastName") String lastName);
    
    @Query("SELECT p FROM Passenger p WHERE UPPER(p.lastName) = UPPER(:lastName)")
    public List<Passenger> getPassengersByLastName(@Param("lastName") String lastName);

    @Query("SELECT p FROM Flight f join f.passengers p where f.id = (:flightId)")
    public List<Passenger> getPassengersByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT p FROM Flight f join f.passengers p where f.id = (:flightId) AND UPPER(p.firstName) = UPPER(:firstName) AND UPPER(p.lastName) = UPPER(:lastName)")
    public List<Passenger> getPassengersByFlightIdAndName(@Param("flightId") Long flightId, @Param("firstName") String firstName,@Param("lastName") String lastName);

    @Query("SELECT d FROM Disposition d where d.passenger.id = (:passengerId) AND d.flight.id = (:flightId)")
    public List<Disposition> getPassengerDispositionHistory(@Param("passengerId") Long passengerId, @Param("flightId") Long flightId);
    
	@Modifying
	@Transactional
    @Query("UPDATE Passenger set watchlistCheckTimestamp =:lastTimestamp WHERE id=:passengerId")
    public void setPassengerWatchlistTimestamp(@Param("passengerId") Long passengerId, @Param("lastTimestamp") Date lastTimestamp);
}
