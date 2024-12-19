import React from "react";
import { Card, CardBody, Col } from "reactstrap";

const TimeSlotCard = ({ startTime = "09:00", endTime = "17:00" }) => {
  return (
    <Card className="mb-3 shadow">
      <CardBody>
        <Col xs="6" className="text-center">
        <p className="font-weight-bold">{startTime}-{endTime}</p>
        </Col>
      </CardBody>
    </Card>
  );
};

export default TimeSlotCard;
