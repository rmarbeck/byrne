# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table invoice (
  id                        bigint not null,
  unique_serial_key         varchar(255),
  creation_date             timestamp,
  last_modification_date    timestamp,
  invoice_date              timestamp,
  customer_type_of_invoice  varchar(255),
  brands                    varchar(255),
  movements                 varchar(255),
  serials                   varchar(255),
  cases                     varchar(255),
  straps                    varchar(255),
  dials                     varchar(255),
  customer                  varchar(10000),
  check_items               boolean,
  parts_cost                float,
  shipping_cost             float,
  remarks                   varchar(10000),
  constraint uq_invoice_unique_serial_key unique (unique_serial_key),
  constraint pk_invoice primary key (id))
;

create table invoice_item (
  id                        bigint not null,
  name                      varchar(255),
  cost                      float,
  no_vat                    boolean,
  invoice_id                bigint,
  constraint pk_invoice_item primary key (id))
;

create table preset_customer (
  id                        bigint not null,
  short_name                varchar(255),
  name_and_address          varchar(10000),
  remarks                   varchar(10000),
  constraint pk_preset_customer primary key (id))
;

create sequence invoice_seq;

create sequence invoice_item_seq;

create sequence preset_customer_seq;

alter table invoice_item add constraint fk_invoice_item_invoice_1 foreign key (invoice_id) references invoice (id) on delete restrict on update restrict;
create index ix_invoice_item_invoice_1 on invoice_item (invoice_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists invoice;

drop table if exists invoice_item;

drop table if exists preset_customer;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists invoice_seq;

drop sequence if exists invoice_item_seq;

drop sequence if exists preset_customer_seq;

