--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // Bootstrap.sql

-- This is the only SQL script file that is NOT
-- a valid migration and will not be run or tracked
-- in the changelog.  There is no @UNDO section.

-- // Do I need this file?

-- New projects likely won't need this file.
-- Existing projects will likely need this file.
-- It's unlikely that this bootstrap should be run
-- in the production environment.

-- // Purpose

-- The purpose of this file is to provide a facility
-- to initialize the database to a state before MyBatis
-- SQL migrations were applied.  If you already have
-- a database in production, then you probably have
-- a script that you run on your developer machine
-- to initialize the database.  That script can now
-- be put in this bootstrap file (but does not have
-- to be if you are comfortable with your current process.

-- // Running

-- The bootstrap SQL is run with the "migrate bootstrap"
-- command.  It must be run manually, it's never run as
-- part of the regular migration process and will never
-- be undone. Variables (e.g. ${variable}) are still
-- parsed in the bootstrap SQL.

-- After the boostrap SQL has been run, you can then
-- use the migrations and the changelog for all future
-- database change management.
create table image
(
    image_id  numeric(10) NOT NULL primary key,
    image_data  bytea
);

create table product
(
    product_id   numeric(20) NOT NULL primary key,
    product_name varchar(50) NOT NULL,
    product_description varchar(200),
    product_price numeric(10, 5),
    stock_quantity numeric(10),
    soled_quantity numeric(10),
    image_id numeric(10) references image (image_id)
);

create table users
(
    user_id numeric(20) NOT NULL primary key,
    user_name varchar(50) NOT NULL,
    password varchar(200) NOT NULL
);

create table orders
(
    order_id numeric(20) NOT NULL primary key,
    user_id numeric(20) NOT NULL references users (user_id),
    order_confirmation varchar(10),
    status varchar(10)
);

create table product_order
(
    order_id numeric(20) NOT NULL references orders (order_id),
    product_id numeric(20) NOT NULL references product (product_id),
    primary key(order_id, product_id)
);