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
  const handleOrderSubmit = (order) => {
    if (items.length === 0) {
      alert("아이템을 추가해 주세요!");
    } else {
      axios.post('http://localhost:8080/api/v1/orders', {
        nickName: order.nickName,
        orderItems: items.map(v => ({
          coffeeId: v.id,
          category: v.category,
          price: v.price,
          quantity: v.count
        }))
      }).then(v => alert("주문이 정상적으로 접수되었습니다."),
          e => {
            alert("서버 장애");
            console.error(e);
          })
    }
  }

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
              <Summary items={items} onOrderSubmit={handleOrderSubmit}/>
            </div>
          </div>
        </div>
      </div>
  );
}

export default App;
