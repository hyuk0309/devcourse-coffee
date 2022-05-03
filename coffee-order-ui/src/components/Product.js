import React from "react";
import coffeePath from './coffee.jpeg'

export function Product(props) {
  const id = props.id;
  const productName = props.productName;
  const category = props.category;
  const price = props.price;
  const handleAddBtnClicked = e => {
    props.onAddClick(id);
  };
  return (
      <>
        <div className="col-2"><img className="img-fluid"
                                    src={coffeePath}
                                    alt=""/></div>
        <div className="col">
          <div className="row text-muted">{category}</div>
          <div className="row">{productName}</div>
        </div>
        <div className="col text-center price">{price}원</div>
        <div className="col text-end action">
          <button
              onClick={handleAddBtnClicked}
              className="btn btn-small btn-outline-dark">추가
          </button>
        </div>
      </>
  )
}