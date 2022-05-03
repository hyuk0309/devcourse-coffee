import './App.css';
import 'bootstrap/dist/css/bootstrap.css'
import {useEffect, useState} from "react";
import {CoffeeList} from "./components/CoffeeList";
import {Summary} from "./components/Summary";
import axios from "axios";

function App() {
  const [coffees, setCoffees] = useState([])
  const [items, setItems] = useState([])
  const handleAddClicked = id => {
    const coffee = coffees.find(v => v.id === id);
    const found = items.find(v => v.id === id);
    const updatedItems =
        found ? items.map(v => (v.id === id) ? {...v, count: v.count + 1} : v)
            : [...items, {...coffee, count: 1}];
    setItems(updatedItems);
  }
  useEffect(() => {
    axios.get('http://localhost:8080/api/v1/coffees')
        .then(v => setCoffees(v.data));
  }, []);

  return (
      <div className="container-fluid">
        <div className="row justify-content-center m-4">
          <h1 className="text-center">Hyuk's Cafe</h1>
        </div>
        <div className="card">
          <div className="row">
            <div
                className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
              <CoffeeList coffees={coffees} onAddClick={handleAddClicked}/>
            </div>
            <div className="col-md-4 summary p-4">
              <Summary items={items}/>
            </div>
          </div>
        </div>
      </div>
  );
}

export default App;
