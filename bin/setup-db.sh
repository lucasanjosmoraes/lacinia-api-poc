#!/usr/bin/env bash

docker exec -i --user postgres cgg_db_1 createdb cggdb

docker exec -i --user postgres cgg_db_1 psql cggdb -a <<__END
create user cgg_role password 'lacinia';
__END

docker exec -i cgg_db_1 psql -Ucgg_role cggdb -a <<__END
drop table if exists designer_to_game;
drop table if exists game_rating;
drop table if exists member;
drop table if exists board_game;
drop table if exists designer;

CREATE OR REPLACE FUNCTION mantain_updated_at()
RETURNS TRIGGER AS \$\$
BEGIN
   NEW.updated_at = now();
   RETURN NEW;
END;
\$\$ language 'plpgsql';

create table member (
  member_id int generated by default as identity primary key,
  name text not null,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp);

create trigger member_updated_at before update
on member for each row execute procedure
mantain_updated_at();

create table board_game (
  game_id int generated by default as identity primary key,
  name text not null,
  summary text,
  min_players integer,
  max_players integer,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp);

create trigger board_game_updated_at before update
on board_game for each row execute procedure
mantain_updated_at();

create table designer (
  designer_id int generated by default as identity primary key,
  name text not null,
  url text,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp);

create trigger designer_updated_at before update
on designer for each row execute procedure
mantain_updated_at();

create table game_rating (
  game_id int references board_game(game_id),
  member_id int references member(member_id),
  rating integer not null,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp);

alter table game_rating add constraint cost_unique__game_rating_game_id_member_id
unique (game_id, member_id);

create trigger game_rating_updated_at before update
on game_rating for each row execute procedure
mantain_updated_at();

create table designer_to_game (
  designer_id int  references designer(designer_id),
  game_id int  references board_game(game_id),
  primary key (designer_id, game_id));

insert into board_game (game_id, name, summary, min_players, max_players) values
  (1234, 'Zertz', 'Two player abstract with forced moves and shrinking board', 2, 2),
  (1235, 'Dominion', 'Created the deck-building genre; zillions of expansions', 2, null),
  (1236, 'Tiny Epic Galaxies', 'Fast dice-based sci-fi space game with a bit of chaos', 1, 4),
  (1237, '7 Wonders: Duel', 'Tense, quick card game of developing civilizations', 2, 2);

alter table board_game alter column game_id restart with 1300;

insert into member (member_id, name) values
  (37, 'curiousattemptbunny'),
  (1410, 'bleedingedge'),
  (2812, 'missyo');

alter table member alter column member_id restart with 2900;

insert into designer (designer_id, name, url) values
  (200, 'Kris Burm', 'http://www.gipf.com/project_gipf/burm/burm.html'),
  (201, 'Antoine Bauza', 'http://www.antoinebauza.fr/'),
  (202, 'Bruno Cathala', 'http://www.brunocathala.com/'),
  (203, 'Scott Almes', null),
  (204, 'Donald X. Vaccarino', null);

alter table designer alter column designer_id restart with 300;

insert into designer_to_game (designer_id, game_id) values
  (200, 1234),
  (201, 1237),
  (204, 1235),
  (203, 1236),
  (202, 1237);

insert into game_rating (game_id, member_id, rating) values
  (1234, 37, 3),
  (1234, 1410, 5),
  (1236, 1410, 4),
  (1237, 1410, 4),
  (1237, 2812, 4),
  (1237, 37, 5);
__END
