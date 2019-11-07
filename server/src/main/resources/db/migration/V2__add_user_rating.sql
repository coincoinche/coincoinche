ALTER TABLE "user"
  ADD COLUMN "rating" integer NOT NULL DEFAULT 1000,
  ADD COLUMN "rating_adjustment" integer NOT NULL DEFAULT 32;

ALTER TABLE "user" ALTER COLUMN "rating" DROP DEFAULT;
ALTER TABLE "user" ALTER COLUMN "rating_adjustment" DROP DEFAULT;

ALTER TABLE "user" ALTER COLUMN "username" SET NOT NULL;
