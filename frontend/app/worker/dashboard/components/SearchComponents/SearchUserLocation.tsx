import React from "react";
import InputItem from "./InputItem";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";

const SearchUserLocation = ({ session }: { session: RequestCookie }) => {
  return (
    <div className="w-full  rounded-[10px] shadow-lg ">
      <InputItem session={session} />
    </div>
  );
};

export default SearchUserLocation;
