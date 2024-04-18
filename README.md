# Darwin_World
This project is a dynamic simulation of animal evolution. Application provided graphic interfce implemented using JavaFX. Every step of the simulation included:
- Removing dead animals from the map
- Turning and moving each animal
- Consumption of plants on which animals have entered fields
- Reproduction of animals located in the same field
- Growth of new plants on selected fields of the map
  
The given simulation is described by a series of parameters:
- Height and width of the map,
- Map variant,
- Initial number of plants,
- Energy provided by consuming one plant,
- Number of plants growing each day,
- Plant growth variant,
- Initial number of animals,
- Initial energy of animals,
- Energy required to consider an animal satiated,
- Energy of parents used to create offspring,
- Minimum and maximum number of mutations in offspring (can be equal to 0),
- Mutation variant,
- Length of animal genomes,
- Animal behavior variant.

## Main functionalities:
- Starting a simulation with selected parameters
  - Selecting a previously prepared configuration
  - Create new confuguration
  - Save configuration
- Ability to run multiple simulations simultaneously (each in a different window)
- Animation showing the positions of animals, their energy, the positions of plants - and their changes
- Stopping and resuming the simulation
- Tracking the following statistics for the current situation in the simulation:
  - Number of all animals,
  - Number of all plants,
  - Number of free fields,
  - Most common genotypes,
  - Average energy level for living animals,
  - Average lifespan of animals for dead animals,
  - Average number of offspring for living animals.
- Saving simulation statistics to CSV file

![obraz](https://github.com/kaDebowska/Darwin_World/assets/62252190/636deff9-ba82-4db8-ab5b-b6c770a11f72)

![obraz](https://github.com/kaDebowska/Darwin_World/assets/62252190/6f7ab2eb-2fd9-49ec-b5b3-f251fb88d963)



