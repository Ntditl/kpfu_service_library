import React from "react";
import "./BookCover.css";

function BookCover({ image }) {
  return (
    <div className="book-cover">
      <img src={image} alt="Обложка книги" />
    </div>
  );
}

export default BookCover;