```sql

UPDATE bay SET bay_name = 'BAY01' where bay_name = 'BAY1';
UPDATE bay SET bay_name = 'BAY02' where bay_name = 'BAY2';
UPDATE bay SET bay_name = 'BAY03' where bay_name = 'BAY3';
UPDATE bay SET bay_name = 'BAY04' where bay_name = 'BAY4';
UPDATE bay SET bay_name = 'BAY05' where bay_name = 'BAY5';
UPDATE bay SET bay_name = 'BAY06' where bay_name = 'BAY6';
UPDATE bay SET bay_name = 'BAY07' where bay_name = 'BAY7';
UPDATE bay SET bay_name = 'BAY08' where bay_name = 'BAY8';
UPDATE bay SET bay_name = 'BAY09' where bay_name = 'BAY9';

UPDATE device SET bay = 'BAY01' where bay = 'BAY1';
UPDATE device SET bay = 'BAY02' where bay = 'BAY2';
UPDATE device SET bay = 'BAY03' where bay = 'BAY3';
UPDATE device SET bay = 'BAY04' where bay = 'BAY4';
UPDATE device SET bay = 'BAY05' where bay = 'BAY5';
UPDATE device SET bay = 'BAY06' where bay = 'BAY6';
UPDATE device SET bay = 'BAY07' where bay = 'BAY7';
UPDATE device SET bay = 'BAY08' where bay = 'BAY8';
UPDATE device SET bay = 'BAY09' where bay = 'BAY9';

UPDATE device_history SET bay = 'BAY01' where bay = 'BAY1';
UPDATE device_history SET bay = 'BAY02' where bay = 'BAY2';
UPDATE device_history SET bay = 'BAY03' where bay = 'BAY3';
UPDATE device_history SET bay = 'BAY04' where bay = 'BAY4';
UPDATE device_history SET bay = 'BAY05' where bay = 'BAY5';
UPDATE device_history SET bay = 'BAY06' where bay = 'BAY6';
UPDATE device_history SET bay = 'BAY07' where bay = 'BAY7';
UPDATE device_history SET bay = 'BAY08' where bay = 'BAY8';
UPDATE device_history SET bay = 'BAY09' where bay = 'BAY9';

UPDATE device_log SET bay = 'BAY01' where bay = 'BAY1';
UPDATE device_log SET bay = 'BAY02' where bay = 'BAY2';
UPDATE device_log SET bay = 'BAY03' where bay = 'BAY3';
UPDATE device_log SET bay = 'BAY04' where bay = 'BAY4';
UPDATE device_log SET bay = 'BAY05' where bay = 'BAY5';
UPDATE device_log SET bay = 'BAY06' where bay = 'BAY6';
UPDATE device_log SET bay = 'BAY07' where bay = 'BAY7';
UPDATE device_log SET bay = 'BAY08' where bay = 'BAY8';
UPDATE device_log SET bay = 'BAY09' where bay = 'BAY9';

commit;

```