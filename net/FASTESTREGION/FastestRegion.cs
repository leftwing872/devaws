using System.Diagnostics;
using Amazon;
using Amazon.EC2;
public class FastestRegion
{
    static async Task Main(string[] args)
    {
        AmazonEC2Client[] clients = {
            new AmazonEC2Client(RegionEndpoint.USEast1),
            new AmazonEC2Client(RegionEndpoint.USEast2),
            new AmazonEC2Client(RegionEndpoint.USWest1),
            new AmazonEC2Client(RegionEndpoint.APNortheast1),
            new AmazonEC2Client(RegionEndpoint.APNortheast2),
            new AmazonEC2Client(RegionEndpoint.APNortheast3),
            new AmazonEC2Client(RegionEndpoint.EUCentral1),
            new AmazonEC2Client(RegionEndpoint.EUNorth1),
            new AmazonEC2Client(RegionEndpoint.EUWest1),
            new AmazonEC2Client(RegionEndpoint.EUWest2),
            new AmazonEC2Client(RegionEndpoint.EUWest3),
            new AmazonEC2Client(RegionEndpoint.SAEast1),
            new AmazonEC2Client(RegionEndpoint.APSoutheast1),
            new AmazonEC2Client(RegionEndpoint.APSoutheast2)
        };

        var tasks = clients.Select(async client => {
            var task1watch = new Stopwatch();
            task1watch.Start();
            Console.WriteLine("Gettring regions.");
            var t = await client.DescribeRegionsAsync();
            Console.WriteLine($" {client.Config.RegionEndpoint} -> {task1watch.ElapsedMilliseconds}");
            return t;
            });
        await Task.WhenAll(tasks);

    }
}
