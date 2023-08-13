import { useState } from "react";
import House from "public/svg/test/house.svg";
export function HelloComponent() {
  const [counter, setCounter] = useState(0);
  return (
    <div className="flex flex-col justify-center items-center">
      <div className="text-xl text-blue-600">Hello from Component!</div>
      <House className="w-10 h-10" />
      <div>
        <p>
          counter: <span>{counter}</span>
        </p>
        <button
          className="bg-red-600 w-8 h-8 rounded m-1"
          onClick={() => setCounter(counter - 1)}
        >
          -
        </button>
        <button
          className="bg-blue-600 w-8 h-8 rounded m-1"
          onClick={() => setCounter(counter + 1)}
        >
          +
        </button>
      </div>
    </div>
  );
}
