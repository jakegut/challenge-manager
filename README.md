# challenge-plugin
Spigot 1.12.2 Plugin that allows admins to easily edit Challenges and freeze/unfreeze players


## ChallengeSets and Challenges
The ChallengeManager has an ArrayList of ChallengeSets.

An instance of a ChallengeSet has a title, a UUID, and an ArrayList of Challenges.

An instance of a Challenge has a description and a UUID.
So...a ChallengeSet has mutliple Challenges.

## Using the Challenge Manager
1. Have the `challenges.admin` command
2. Create a new challenge set: `/challenge create <title here (one word)>`
3. Add a new challenge: `/challenge add <description here>`
4. List all challenge sets: `/challenge list`
5. List challenges in a set: `/challenge list <set title here>`
6. Edit the title of a set:: `/challenge change title <new title here>`
7. Edit the descrption of a challenge: `/challenge change <number> <description>`
(Use `/challenge list <set title here>` and decide which challenge to change by the number on the side)
8. Change the Challenge Set you're editing: `/challenge edit <title>`
9. Set player(s) to use a Challenge Set: `/challenge set <ChallengeSet> player <players>`


## Permissions
* challenges.freeze
* challenges.unfreeze
* challenges.admin
