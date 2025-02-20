import React from "react";
import { Stack } from "@mui/material";

const ProjectOverview: React.FC = () => {
  return (
    <Stack spacing={3}>
      <div>
        <h2>Welcome to wirespec showcase</h2>
        <p>
          We're happy to have you around, exploring the capabilities of wirespec
        </p>
      </div>

      <div>
        <h2>The landscape / setup of wirespec showcase</h2>
      </div>
      <div>
        <h2>The contracts</h2>
      </div>
    </Stack>
  );
};

export default ProjectOverview;
