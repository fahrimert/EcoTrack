package com.example.EcoTrack.pdfReports.repository;

import com.example.EcoTrack.pdfReports.model.PdfReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import  java.util.List;

public interface PdfRepository extends JpaRepository<PdfReports,Long> {

    boolean existsByStartTimeAndSupervisorId (String startTime, long l);

    @Query(value = "SELECT * FROM pdf_reports  WHERE supervisor_id = :id", nativeQuery = true)

    List<PdfReports> findBySupervisorId(@Param("id") Long supervisorId);
}

