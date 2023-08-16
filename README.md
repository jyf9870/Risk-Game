# ece651-sp23-project1

![pipeline](https://gitlab.oit.duke.edu/yj193/ece651-sp23-project1/badges/master/pipeline.svg)

![coverage](https://gitlab.oit.duke.edu/yj193/ece651-sp23-project1/badges/master/coverage.svg?job=test)

## Coverage
[Detailed coverage](https://yj193.pages.oit.duke.edu/ece651-sp23-project1/dashboard.html)

## Fine-tune Constants Explained

We decided to use a constant C of 1 for the food resource cost for both move and attack.
This made sense because the way we designed the food resource initialization and production rates,
as well as the distances between territories.

Another design choice was to simply have each unit be the same amount of food resource, even though one might
be more powerful than the other. The reason we chose this is that in our opinion, by costing more to move a stronger
unit, it de-incentivizes a player to upgrade their units (because it will be harder to move / attack with them).

## Distances

We tried to make the distances between players' own territories low, and the distances between a player's own territory
and
enemy territories higher. This is so that the moving of units in the beginning isn't as harsh, and it incentivizes
players to wait a few
turns before attacking (makes gameplay more interesting).

Also, we gave a slight edge to the player with 1 less territory when it comes to distances because they are starting
with a natural disadvantage.
There were other design choices made like having certain territories that are susceptible to attacks form many angles,
have close distances to nearby
supporting territories. This allows the game to be more competitive because a player can put a lot of resources into
defending these susceptible
territories.

## Upgrade and Technology

We decided to keep bonuses on upgraded units as well as costs, the same as what was advised in the readme. We adjusted
initial resources
and rates so that it could allow players to upgrade off the bat, but not too quickly. Also, we decided it made sense
that a player needed to
wait roughly 3 turns until they could upgrade a unit to level 7 (assuming they didn't upgrade anything else). This will
allow for a powerful siege
but also allows for more spread out upgrading.