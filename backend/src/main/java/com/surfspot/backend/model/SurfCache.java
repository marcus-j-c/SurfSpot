package com.surfspot.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity /* treat this java class as a postgres database table */
@Table(name = "surf_cache") /* name the database */
public class SurfCache {
}
