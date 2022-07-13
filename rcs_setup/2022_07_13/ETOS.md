```sql
drop table if exists device_history;
drop table if exists edge_history;
drop table if exists alarm_history;


create table alarm_history (
	alarm_history_id bigint not null,
	alarm_time datetime not null,
	content varchar(255),
	alarm_clear_comment varchar(255),
	factory varchar(40),
	area varchar(40),
	bay varchar(40),
	target varchar(40),
	edge_id bigint,
	edge_name varchar(40),
	edge_status varchar(40),
	device_id bigint,
	device_name varchar(40),
	device_status varchar(40),
	action varchar(40),
	created_by varchar(128),
	created_date datetime,
	last_modifed_by varchar(128),
	last_modified_date datetime,
	event_comment varchar(255),
	event_name varchar(40),
	primary key (alarm_history_id, alarm_time)
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX alarm_history_i1 ON alarm_history (alarm_time);

alter table alarm_history PARTITION BY RANGE (TO_DAYS( alarm_time )) (
	PARTITION p_2022_07 VALUES LESS THAN  (TO_DAYS('2022-08-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_08 VALUES LESS THAN  (TO_DAYS('2022-09-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_09 VALUES LESS THAN  (TO_DAYS('2022-10-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_10 VALUES LESS THAN  (TO_DAYS('2022-11-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_11 VALUES LESS THAN  (TO_DAYS('2022-12-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_12 VALUES LESS THAN  (TO_DAYS('2023-01-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_01 VALUES LESS THAN  (TO_DAYS('2023-02-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_02 VALUES LESS THAN  (TO_DAYS('2023-03-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_03 VALUES LESS THAN  (TO_DAYS('2023-04-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_04 VALUES LESS THAN  (TO_DAYS('2023-05-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_05 VALUES LESS THAN  (TO_DAYS('2023-06-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_06 VALUES LESS THAN  (TO_DAYS('2023-07-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_07 VALUES LESS THAN  (TO_DAYS('2023-08-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_08 VALUES LESS THAN  (TO_DAYS('2023-09-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_09 VALUES LESS THAN  (TO_DAYS('2023-10-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_10 VALUES LESS THAN  (TO_DAYS('2023-11-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_11 VALUES LESS THAN  (TO_DAYS('2023-12-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_12 VALUES LESS THAN  (TO_DAYS('2024-01-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_01 VALUES LESS THAN  (TO_DAYS('2024-02-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_02 VALUES LESS THAN  (TO_DAYS('2024-03-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_03 VALUES LESS THAN  (TO_DAYS('2024-04-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_04 VALUES LESS THAN  (TO_DAYS('2024-05-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_05 VALUES LESS THAN  (TO_DAYS('2024-06-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_06 VALUES LESS THAN  (TO_DAYS('2024-07-01 00:00:00')) ENGINE = InnoDB
);



create table device_history (
	device_history_id bigint not null auto_increment,
	device_id bigint,
	device_name varchar(40),
	device_type varchar(40),
	edge_id bigint,
	factory varchar(40),
	area varchar(40),
	bay varchar(40),
	target varchar(40),
	edge_name varchar(40),
	edge_status integer,
	sw_name varchar(40),
	sw_version varchar(40),
	patch_type varchar(40),
	patch_time datetime,
	patch_user varchar(128),
	patch_version varchar(40),
	cmd varchar(255),
	process_id bigint,
	status varchar(40),
	action varchar(40),
	created_by varchar(128),
	created_date datetime,
	last_modifed_by varchar(128),
	last_modified_date datetime not null,
	event_comment varchar(255),
	event_name varchar(40),
	primary key (device_history_id, last_modified_date)
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX device_history_i1 ON device_history (last_modified_date);

alter table device_history PARTITION BY RANGE (TO_DAYS( last_modified_date )) (
	PARTITION p_2022_07 VALUES LESS THAN  (TO_DAYS('2022-08-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_08 VALUES LESS THAN  (TO_DAYS('2022-09-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_09 VALUES LESS THAN  (TO_DAYS('2022-10-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_10 VALUES LESS THAN  (TO_DAYS('2022-11-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_11 VALUES LESS THAN  (TO_DAYS('2022-12-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_12 VALUES LESS THAN  (TO_DAYS('2023-01-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_01 VALUES LESS THAN  (TO_DAYS('2023-02-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_02 VALUES LESS THAN  (TO_DAYS('2023-03-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_03 VALUES LESS THAN  (TO_DAYS('2023-04-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_04 VALUES LESS THAN  (TO_DAYS('2023-05-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_05 VALUES LESS THAN  (TO_DAYS('2023-06-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_06 VALUES LESS THAN  (TO_DAYS('2023-07-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_07 VALUES LESS THAN  (TO_DAYS('2023-08-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_08 VALUES LESS THAN  (TO_DAYS('2023-09-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_09 VALUES LESS THAN  (TO_DAYS('2023-10-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_10 VALUES LESS THAN  (TO_DAYS('2023-11-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_11 VALUES LESS THAN  (TO_DAYS('2023-12-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_12 VALUES LESS THAN  (TO_DAYS('2024-01-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_01 VALUES LESS THAN  (TO_DAYS('2024-02-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_02 VALUES LESS THAN  (TO_DAYS('2024-03-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_03 VALUES LESS THAN  (TO_DAYS('2024-04-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_04 VALUES LESS THAN  (TO_DAYS('2024-05-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_05 VALUES LESS THAN  (TO_DAYS('2024-06-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_06 VALUES LESS THAN  (TO_DAYS('2024-07-01 00:00:00')) ENGINE = InnoDB
);




create table edge_history (
	edge_history_id bigint not null auto_increment,
	edge_id bigint,
	edge_name varchar(40),
	factory varchar(40),
	area varchar(40),
	bay varchar(40),
	target varchar(40),
	status varchar(40),
	active bit not null,
	hw_model varchar(40),
	hw_serial_no varchar(255),
	os_name varchar(40),
	os_version varchar(40),
	processor_capa varchar(40),
	memory_capa varchar(40),
	disk_capa varchar(40),
	ethernet_port_count integer,
	serial_port_count integer,
	curr_cpu bigint,
	curr_cpu_temp bigint,
	curr_disk bigint,
	curr_memory bigint,
	patch_time datetime,
	startup_time datetime,
	down_cause varchar(255),
	action varchar(40),
	created_by varchar(128),
	created_date datetime,
	last_modifed_by varchar(128),
	last_modified_date datetime not null,
	event_comment varchar(255),
	event_name varchar(40),
	primary key (edge_history_id, last_modified_date)
) engine=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX edge_history_i1 ON edge_history (last_modified_date);

alter table edge_history PARTITION BY RANGE (TO_DAYS( last_modified_date )) (
	PARTITION p_2022_07 VALUES LESS THAN  (TO_DAYS('2022-08-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_08 VALUES LESS THAN  (TO_DAYS('2022-09-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_09 VALUES LESS THAN  (TO_DAYS('2022-10-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_10 VALUES LESS THAN  (TO_DAYS('2022-11-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_11 VALUES LESS THAN  (TO_DAYS('2022-12-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2022_12 VALUES LESS THAN  (TO_DAYS('2023-01-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_01 VALUES LESS THAN  (TO_DAYS('2023-02-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_02 VALUES LESS THAN  (TO_DAYS('2023-03-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_03 VALUES LESS THAN  (TO_DAYS('2023-04-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_04 VALUES LESS THAN  (TO_DAYS('2023-05-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_05 VALUES LESS THAN  (TO_DAYS('2023-06-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_06 VALUES LESS THAN  (TO_DAYS('2023-07-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_07 VALUES LESS THAN  (TO_DAYS('2023-08-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_08 VALUES LESS THAN  (TO_DAYS('2023-09-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_09 VALUES LESS THAN  (TO_DAYS('2023-10-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_10 VALUES LESS THAN  (TO_DAYS('2023-11-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_11 VALUES LESS THAN  (TO_DAYS('2023-12-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2023_12 VALUES LESS THAN  (TO_DAYS('2024-01-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_01 VALUES LESS THAN  (TO_DAYS('2024-02-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_02 VALUES LESS THAN  (TO_DAYS('2024-03-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_03 VALUES LESS THAN  (TO_DAYS('2024-04-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_04 VALUES LESS THAN  (TO_DAYS('2024-05-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_05 VALUES LESS THAN  (TO_DAYS('2024-06-01 00:00:00')) ENGINE = InnoDB,
	PARTITION p_2024_06 VALUES LESS THAN  (TO_DAYS('2024-07-01 00:00:00')) ENGINE = InnoDB
);


```