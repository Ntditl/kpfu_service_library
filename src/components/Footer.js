import React from "react";
import "./Footer.css";

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-section1">
            <img src="/images/log-footer.png" width="131" height="70"/>
        </div>
        
        <div className="footer-section2">
            <div>Адрес: 420015, Татарстан, г. Казань, ул. Пушкина, 86</div>
            <div>Телефон: (843) 222-82-85</div>
            <div>E-Mail: Director.Nbrt@tatar.ru</div>
        </div>
        
      </div>
    </footer>
  );
}

export default Footer;