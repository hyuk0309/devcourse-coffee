import {SummaryItem} from "./SummaryItem";

export function Summary({items = []}) {
  const totalPrice = items.reduce(
      (prev, curr) => prev + (curr.price * curr.count), 0)
  return (
      <>
        <div>
          <h5 className="m-0 p-0"><b>Order Summary</b></h5>
        </div>
        <hr/>
        {items.map(v => <SummaryItem key={v.id} count={v.count}
                                     productName={v.productName}/>)}
        <form>
          <div className="mb-3">
            <label htmlFor="nickName" className="form-label">닉네임</label>
            <input type="text" className="form-control mb-1" id="nickName"/>
          </div>

          <div>이용해 주셔서 감사합니다:)</div>
        </form>
        <div className="row pt-2 pb-2 border-top">
          <h5 className="col">총금액</h5>
          <h5 className="col text-end">{totalPrice}원</h5>
        </div>
        <button className="btn btn-dark col-12">결제하기</button>
      </>
  )
}