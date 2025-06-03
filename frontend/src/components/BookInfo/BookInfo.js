import React from "react";
import "./BookInfo.css";

function BookInfo({ title, author, year, publish, isbn, description}) {
  return (
    <div className="book-info">
      <h1>{title}</h1>
      <h2>{author}</h2>
      
      <div className="book-meta">
        <span>Год издания: {year} </span>
        <span>Издательство: {publish}</span>
        <span>ISBN: {isbn}</span>
      </div>
      
      <div className="book-description">
        <h3>О книге</h3>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default BookInfo;