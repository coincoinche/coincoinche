CREATE TABLE "user" (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     username VARCHAR(25) UNIQUE
);
