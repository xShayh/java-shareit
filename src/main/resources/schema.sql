DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
    user_id long generated by default as identity primary key,
    user_name varchar(255) not null,
    email varchar(512) not null unique
);

CREATE TABLE IF NOT EXISTS requests (
    request_id long generated by default as identity primary key,
    description varchar(512) not null,
    requester_id long not null REFERENCES users (user_id) on delete cascade,
    date_created timestamp without time zone
);

CREATE TABLE IF NOT EXISTS items (
    item_id long generated by default as identity primary key,
    item_name varchar(255) not null,
    description varchar(512) not null,
    is_available boolean not null,
    owner_id long not null REFERENCES users (user_id) on delete cascade,
    request_id long REFERENCES requests (request_id)
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id long generated by default as identity primary key,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    item_id long not null REFERENCES items (item_id) on delete cascade,
    booker_id long not null REFERENCES users (user_id) on delete cascade,
    status varchar(16) not null
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id long generated by default as identity primary key,
    text varchar(512) not null,
    item_id long not null REFERENCES items (item_id) on delete cascade,
    author_id long not null REFERENCES users (user_id) on delete cascade,
    created timestamp without time zone
);