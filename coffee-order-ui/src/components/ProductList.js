import {Product} from "./Product";

export function ProductList({products = [], onAddClick}) {
  return (
      <>
        <h5 className="flex-grow-0"><b>Coffees</b></h5>
        <ul className="list-group products">
          {products.map(v =>
              <li key={v.id} className="list-group-item d-flex mt-3">
                <Product {...v} onAddClick={onAddClick}/>
              </li>
          )}
        </ul>
      </>
  )
}