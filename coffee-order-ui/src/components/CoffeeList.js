import {Coffee} from "./Coffee";

export function CoffeeList({coffees = [], onAddClick}) {
  return (
      <>
        <h5 className="flex-grow-0"><b>Coffees</b></h5>
        <ul className="list-group products">
          {coffees.map(v =>
              <li key={v.id} className="list-group-item d-flex mt-3">
                <Coffee {...v} onAddClick={onAddClick}/>
              </li>
          )}
        </ul>
      </>
  )
}